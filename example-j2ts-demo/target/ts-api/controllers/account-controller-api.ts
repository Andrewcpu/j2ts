/* tslint:disable */
/* eslint-disable */
import * as backend from '../types'
const request = require('axios');


/**
 * Update a user model
 * 
 * @param {backend.IUser} user - Updated user model
 * @returns {backend.IUser} The updated user model.
 */
export function updateUser(user: backend.IUser): Promise<backend.IUser> {
    return request.post("/user", user);
}

/**
 * Get a user from their userID
 * 
 * @param {string} userId - UserID to search
 * @param {string} q - Query parameter
 * @returns {backend.IUser} The requested user model.
 */
export function getUserById(userId: string, q: string): Promise<backend.IUser> {
    return request.get(`/user/${userId}`, {
      params: {
        q
      }
    });
}