/* tslint:disable */
/* eslint-disable */
import * as backend from '../types'
const request = require('axios');


/**
 * Get a user phone number by userID and type
 * 
 * @param {string} userId
 * @param {backend.IPhoneType} type
 * @param {string} ssoToken
 * @returns {string} 
 */
export function getPhoneNumber(userId: string, type: backend.IPhoneType, ssoToken: string): Promise<string> {
    return request.get(`/phone/${userId}`, ssoToken, {
      params: {
        type
      }
    });
}