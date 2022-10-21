# capacitor-plugin-biometric

biometric login with FaceId, Fingerprint and Iris. Depending to the device biometrics options.

## Install

```bash
npm install capacitor-plugin-biometric
npx cap sync
```

## API

<docgen-index>

* [`saveUser(...)`](#saveuser)
* [`getUser()`](#getuser)
* [`has(...)`](#has)
* [`isAvailable()`](#isavailable)
* [`verify(...)`](#verify)
* [`save(...)`](#save)
* [`saveDataUser(...)`](#savedatauser)
* [`getDataUser()`](#getdatauser)
* [`cleanAll()`](#cleanall)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### saveUser(...)

```typescript
saveUser(options: { "name": string; }) => Promise<{ value: string; }>
```

| Param         | Type                           |
| ------------- | ------------------------------ |
| **`options`** | <code>{ name: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### getUser()

```typescript
getUser() => Promise<{ value: string; }>
```

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### has(...)

```typescript
has(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### isAvailable()

```typescript
isAvailable() => Promise<{ value: string; }>
```

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### verify(...)

```typescript
verify(options: { "key": string; "message": string; }) => Promise<{ value: string; }>
```

| Param         | Type                                           |
| ------------- | ---------------------------------------------- |
| **`options`** | <code>{ key: string; message: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### save(...)

```typescript
save(options: { "key": string; "password": string; "userAuthenticationRequired": string; }) => Promise<{ value: string; }>
```

| Param         | Type                                                                                |
| ------------- | ----------------------------------------------------------------------------------- |
| **`options`** | <code>{ key: string; password: string; userAuthenticationRequired: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### saveDataUser(...)

```typescript
saveDataUser(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### getDataUser()

```typescript
getDataUser() => Promise<{ value: string; }>
```

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### cleanAll()

```typescript
cleanAll() => Promise<{ value: string; }>
```

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------

</docgen-api>
# capacitorBiometria
