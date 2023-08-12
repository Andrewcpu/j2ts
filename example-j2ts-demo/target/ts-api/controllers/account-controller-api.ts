/* tslint:disable */
/* eslint-disable */
import * as api from '../types'
const request = require('axios');


/**
 * Get a user from their userID
 * 
 * @param {string} userId - UserID to search
 * @param {string} q - Query parameter
 * @returns {api.IUser} The requested user model.
 */
export function getUserById(userId: string, q: string): Promise<api.IUser> {
    return request.get(`/user/${userId}`, {
      params: {
        q
      }
    }).then((result: any) => result.data);
}

/**
 * Get a list of users
 * 

 * @returns {api.IUser[]} A list of users
 */
export function getUsers(): Promise<api.IUser[]> {
    return request.get("/users").then((result: any) => result.data);
}

/**
 * Update a user model
 * 
 * @param {api.IUser} user - Updated user model
 * @returns {api.IUser} The updated user model.
 */
export function updateUser(user: api.IUser): Promise<api.IUser> {
    return request.post("/user", user).then((result: any) => result.data);
}