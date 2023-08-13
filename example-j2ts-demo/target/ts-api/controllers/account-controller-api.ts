/* tslint:disable */
/* eslint-disable */
import * as api from '../types'
const request = require('axios');


/**
 * Get a list of users
 * 

 * @returns {api.IUser[]} A list of users
 */
export function getUsers(): Promise<api.IUser[]> {
    return request.get("/api/users").then((result: any) => result.data as api.IUser[]);
}

/**
 * Get a user from their userID
 * 
 * @param {string} userId - UserID to search
 * @param {string} q - Query parameter
 * @returns {api.IUser} The requested user model
 */
export function getUserById(userId: string, q: string): Promise<api.IUser> {
    return request.get(`/api/user/${userId}`, {
      params: {
        q
      }
    }).then((result: any) => result.data as api.IUser)
      .then((result: api.IUser) => {
        localStorage.setItem("userId", result.userId);
        return result;
      });
}

/**
 * Update a user model
 * 
 * @param {api.IUser} user - Updated user model
 * @returns {api.IUser} The updated user model/
 */
export function updateUser(user: api.IUser): Promise<api.IUser> {
    return request.post("/api/user", user).then((result: any) => result.data as api.IUser);
}