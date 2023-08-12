/* tslint:disable */
/* eslint-disable */
import * as api from '../types'
const request = require('axios');


/**
 * Get a user phone number by userID and type
 * 
 * @param {string} userId
 * @param {api.IPhoneType} type
 * @param {string} ssoToken
 * @returns {string} 
 */
export function getPhoneNumber(userId: string, type: api.IPhoneType, ssoToken: string): Promise<string> {
    return request.get(`/api/phone/${userId}`, ssoToken, {
      params: {
        type
      }
    }).then((result: any) => result.data);
}