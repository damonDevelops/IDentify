import { Injectable } from '@angular/core';
import CryptoJS from 'crypto-js';

@Injectable({ providedIn: 'root' })
export class CryptoUtilService {
  constructor() {}

  private encryptAES(plaintext: string): string {
    let tempkey = CryptoJS.SHA256(
      JSON.parse(localStorage.getItem('user')!).accessToken,
    ).toString();
    return CryptoJS.AES.encrypt(plaintext, tempkey).toString();
  }

  private decryptAES(ciphertext: string): string {
    let tempkey = CryptoJS.SHA256(
      JSON.parse(localStorage.getItem('user')!).accessToken,
    ).toString();

    try {
      const decrypted = CryptoJS.AES.decrypt(ciphertext, tempkey).toString(
        CryptoJS.enc.Utf8,
      );

      // Log successful decryption
      //console.log(`Decryption successful: ${decrypted}`);

      return decrypted;
    } catch (error) {
      // Log decryption error
      console.error(`Decryption error: ${error}`);
      return ''; // Return an empty string in case of an error
    }
  }

  storeToSession(label: string, data: string) {
    sessionStorage.setItem(label, this.encryptAES(data));
  }

  retrieveFromSession(label: string): string {
    return this.decryptAES(sessionStorage.getItem(label) || '');
  }

  removeFromSession(label: string) {
    sessionStorage.removeItem(label);
  }
}
