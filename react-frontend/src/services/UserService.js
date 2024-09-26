import axios from 'axios';
import { headers } from '../utils';

const apiBaseUrl = process.env.REACT_APP_ADMIN_API_BASE_URL;
const apiAdmin = process.env.REACT_APP_ADMIN_API_BASE_URL;

export const getUsers = async (token) => {
  return await axios.get(`${apiBaseUrl}/users`, {
    headers: headers(token)
  });
}

export const getUserById = async (userId, token) => {
  return await axios.get(`${apiBaseUrl}/user/${userId}`, {
    headers: headers(token)
  });
}

export const registerUser = async (user, token) => {
  const response = await axios.post(`${apiAdmin}/register`, user, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Headers': 'Content-Type',
      'Access-Control-Allow-Methods': 'OPTIONS, DELETE, POST, GET, PATCH, PUT',
    },
  });
  return response;
}

export const updateUser = async (user, userId, token) => {
  return await axios.put(`${apiBaseUrl}/user/${userId}`, user, {
    headers: headers(token)
  });
}

export const deleteUser = async (userId, token) => {
  return await axios.delete(`${apiBaseUrl}/user/${userId}`, {
    headers: headers(token)
  });
}
