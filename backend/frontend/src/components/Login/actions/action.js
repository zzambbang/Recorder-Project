import axios from 'axios';
import { LOGIN_USER } from './types';

export function loginUser(dataTosubmit) {
    const request = axios.post('http://localhost:8080/api/users/account/auth', dataTosubmit).then((response) => response.data);
    return { type: LOGIN_USER, payload: request }
}