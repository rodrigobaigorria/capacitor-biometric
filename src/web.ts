import { WebPlugin } from '@capacitor/core';

import type { BiometriaPlugin } from './definitions';

export class BiometriaWeb extends WebPlugin implements BiometriaPlugin {

  async saveUser(): Promise<{ value: string }> {
    return {value: 'saveUser'};
  }

  async getUser(): Promise<{ value: string; }> {
    return { value: JSON.stringify(localStorage.getItem('user')) };
  }

  async has(options: { value: string }): Promise<{ value: string }> {
    return options;
  }

  async isAvailable(): Promise<{ value: string }> {
    return {
      value:"isAvailable"
    };
}

  async save(): Promise<{ value: string }> {
    return  {value:'save'};

}
async verify(): Promise<{ value: string }> {
  return  {value:'verify'};
  };

async saveDataUser(options: { value: string }): Promise<{ value: string }> {
    return options;
}
;

async getDataUser(): Promise<{ value: string }> {
    return { value: 'getDataUser' };
}

async cleanAll(): Promise<{ value: string }> {
  return { value: 'cleanAll' };
}

}
