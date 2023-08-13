/* tslint:disable */
/* eslint-disable */
import * as api from '../types'
const request = require('axios');


/**
 * Test API endpoint
 * 
 * @param {string} token
 * @returns {api.IUser} XYX
 */
export function doTest(): Promise<api.IUser> {
    return request.get("/api/test", {
      headers: {
        token: localStorage.getItem("token")
      }
    }).then((result: any) => result.data as api.IUser)
      .then((result: api.IUser) => {
        localStorage.setItem("email", result.email);
        return result;
      });
}

/**
 * Test API endpoint
 * 
 * @param {string} token
 * @param {api.IUser} user
 * @returns {api.IUser} XYX
 */
export function doPostTest(user: api.IUser): Promise<api.IUser> {
    return request.post("/api/test", user, {
      headers: {
        token: localStorage.getItem("token")
      }
    }).then((result: any) => result.data as api.IUser)
      .then((result: api.IUser) => {
        localStorage.setItem("email", result.email);
        return result;
      });
}