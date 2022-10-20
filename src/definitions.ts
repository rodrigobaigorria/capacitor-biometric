export interface BiometriaPlugin {
  saveUser(options:{"name": string}): Promise<{ value: string}>;

  getUser(): Promise<{ value: string}>;

  has(options:{value: string}): Promise<{ value: string}>;

  isAvailable(): Promise<{value: string}>;

  verify(options:{"key": string, "message": string}): Promise<{value: string}>;

  save(options:{"key": string, "password": string, "userAuthenticationRequired": string}): Promise<{value: string}>;

  saveDataUser(options: { value: string }): Promise<{ value: string }>;

  getDataUser(): Promise<{ value: string}>;

}