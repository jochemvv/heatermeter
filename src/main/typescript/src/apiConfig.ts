import * as defaultApi from './generated/api';

const CONTEXT_PATH = ''

export const api = new defaultApi.DefaultApi(undefined, window.location.origin + CONTEXT_PATH);
// export const api = new defaultApi.DefaultApi(undefined, "http://localhost:8080" + CONTEXT_PATH);

