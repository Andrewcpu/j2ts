/* tslint:disable */
/* eslint-disable */
import * as api from '../types'
const request = require('axios');
const emptyString = "";


/**
 * Get a user phone number by userID and type
 * 
 * @param {string} userId
 * @param {api.IPhoneType} type
 * @param {string} ssoToken
 * @returns {string} 
 */
export function getPhoneNumber(userId: string, type: api.IPhoneType, ssoToken: string): Promise<string> {
        return request.get(`/api/phone/${userId}`, {
      params: {
        type
      }
    }).then((result: any) => result.data as string);
}