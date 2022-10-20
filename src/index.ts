import { registerPlugin } from '@capacitor/core';

import type { BiometriaPlugin } from './definitions';

const Biometria = registerPlugin<BiometriaPlugin>('Biometria', {
  web: () => import('./web').then(m => new m.BiometriaWeb()),
});

export * from './definitions';
export { Biometria };
