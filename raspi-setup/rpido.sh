#!/bin/bash
#
URL=$(curl -s https://downloads.raspberrypi.org/raspbian_lite_latest | awk -F\" '/http/ { print $2}')
LATEST=$(basename $URL .zip)
zipfile=DIST/$LATEST.zip
RPI_ROOT=sdcard
VERBOSE=1

 vecho() { [ $VERBOSE -lt 1 ] || echo $* >/dev/stderr; }
vvecho() { [ $VERBOSE -lt 2 ] || echo $* >/dev/stderr; }
SUDO() {
        vecho SUDO $*
        sudo $*
}
set_SD() {
        for d in /dev/sd[a-f] ;do
                if sudo gdisk -l $d 2>/dev/null| grep "^Model:.*SD/MMC">/dev/null; then
                        SD=$(basename $d)
                        break;
                fi
        done
}
loopdev_SD() {
        [ -f "$LATEST.img" ] || unzip -x $zipfile $LATEST.img
        LOOP=$(sudo losetup -f)
        SUDO losetup -P $LOOP $LATEST.img
        SD=$(basename ${LOOP}p)
}
is_mounted() {
        mount | grep $(realpath $1) >/dev/null
}
unmount_SD() {
        for i in /sys/block/${SD}/${SD}?;do
                SUDO umount /dev/$(basename $i) 2>/dev/null
        done
}
mount_SD() {
        unmount_SD
        mkdir -p $RPI_ROOT
        SUDO mount /dev/${SD}2 $RPI_ROOT
        SUDO mount /dev/${SD}1 $RPI_ROOT/boot
}
write_SD() {
        unmount_SD
        unzip -p $zipfile $LATEST.img | sudo dd of=/dev/${SD} bs=4M
}
unmount_all() {
        [ ! -z "$RPI_ROOT" ] || return 1
        [ -z "$keep_mount" ] || return 0
        FULLPATH=$(realpath ${RPI_ROOT})
        LOOP=$(mount | grep "/dev/loop[0-9]*p2.*$FULLPATH" | sed 's/p2.*$//')
        # SUDO rm -f ${RPI_ROOT}/usr/bin/qemu-arm-static
        for p in $(mount | grep $FULLPATH | cut -f3 -d' ' | sort -Vr); do
                SUDO umount $p
        done
        for d in $LOOP; do
                SUDO losetup -d $d
                #SUDO rm -f ${d}*
        done
}
usage() {
        unmount_all
        set +x
        [ $# == 0 ] || echo $*
        echo "usage: rpido <options> cmd"
        echo " -w       write raspian to sdcard"
        echo " -h name  sets /etc/hostname on rpi"
        echo " -s       start shell on raspian"
        echo " cmd      chroot rpi cmd"
        echo "installs files from template"
        echo "configures wifi and sshd, and authorized keys"
        exit
}

while getopts ?h:kqsvw opt;do
        case $opt in
        h) hostname=$OPTARG ;;
        k) keep_mount=y ;;
        q) VERBOSE=0 ;;
        s) rpi_shell=y ;;
        v) VERBOSE=$(($VERBOSE+1)) ;;
        w) write_sd=y ;;
        *) usage ;;
        esac
done
shift $(($OPTIND-1))
[ $VERBOSE -lt 2 ] || set -x
if [ "$rpi_shell" = y ]; then
        CMD="bash -i"
else
        CMD="$*"
fi
[ -f $zipfile ] || curl --create-dirs -o $zipfile $URL
set_SD
if [ -z "$SD" ];then
        loopdev_SD
else
        [ -z "$write_sd" ] || write_SD
fi
mount_SD
if [ -z "${RPI_ROOT}" -o ! -f "$RPI_ROOT/etc/rpi-issue" -o ! -f "$RPI_ROOT/boot/issue.txt" ]; then
        usage raspbian root not as expected
fi
SUDO rsync -a template/. $RPI_ROOT
[ -z "$hostname" ] || echo $hostname | sudo tee $RPI_ROOT/etc/hostname >/dev/null

if [ ! -z "$CMD" ]; then
        SUDO rsync /usr/bin/qemu-arm-static ${RPI_ROOT}/usr/bin/
        for f in proc dev sys;do
                is_mounted $RPI_ROOT/$f || SUDO mount --bind /$f $RPI_ROOT/$f
        done
        SUDO chroot ${RPI_ROOT} $CMD
fi

unmount_all
sync