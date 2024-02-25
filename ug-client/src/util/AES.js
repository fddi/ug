import CryptoJS from 'crypto-js';
import { AESKEY } from '../config/client'
const ivStr = 'Z9VXQIL7OHOY3W8N';
export default {
    encrypt: function (text) {
        const key = CryptoJS.enc.Utf8.parse(AESKEY);
        const iv = CryptoJS.enc.Utf8.parse(ivStr);
        const encrypted = CryptoJS.AES.encrypt(text, key,
            { iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Iso10126 });
        return encrypted.toString();
    },
    decrypt: function (text) {
        const key = CryptoJS.enc.Utf8.parse(AESKEY);
        const iv = CryptoJS.enc.Utf8.parse(ivStr);
        const decrypt = CryptoJS.AES.decrypt(text, key,
            { iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Iso10126 });
        let decryptedStr = decrypt.toString(CryptoJS.enc.Base64);
        return decryptedStr.toString();
    }
}