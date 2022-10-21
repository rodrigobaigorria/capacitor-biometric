import Foundation
import Capacitor
import LocalAuthentication



/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(BiometriaPlugin)

public class BiometriaPlugin: CAPPlugin {
    private let implementation = Biometria()

    @objc func has(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        let userDefaults = UserDefaults.standard
        if((userDefaults.string(forKey: value)) != nil){
            print("Hay key")
            call.resolve([
                "value": implementation.has("OK")
            ])
        }else{
            print("No hay key")
            call.resolve([
                "value": implementation.has("ERROR")
            ])
        }
        

        print("imprimimos \(value)")
        call.resolve([
            "value": implementation.has(value)
        ])
    }
    
    
    @objc func isAvailable(_ call: CAPPluginCall) {
        let context = LAContext()
        var type:String = ""
           
           guard !LAContext.biometricsChanged() else {
               // Handle biometrics changed, such as clearing credentials and making the user manually log in
               // Reset LAContext.savedBiometricsPolicyState to nil after doing so
               call.resolve([
                   "value": implementation.isAvailable("niometriaCambio")
               ])
               return
           }
           
                
        if (context.biometryType == .faceID) {
            type = "Face"
        } else if (context.biometryType == .touchID) {
            type = "Finger"
        } else if (context.biometryType == .none){
            type = "none"
        } else {
            type = "nada"
        }
        
                var error: NSError?
                
                if context.canEvaluatePolicy(
                    LAPolicy.deviceOwnerAuthenticationWithBiometrics,
                    error: &error) {
                               
                    // Device can use biometric authentication
                    print("enrolado")
                    call.resolve([
                        "value": implementation.isAvailable(type)
                    ])
                } else {
                    // Device cannot use biometric authentication
                    if let err = error {
                        switch err.code{
                        
                        case LAError.Code.biometryNotEnrolled.rawValue:
                           print("User is not enrolled")
                            call.resolve([
                                "value": implementation.isAvailable("ERROR")
                            ])
                        
                        case LAError.Code.passcodeNotSet.rawValue:
                            print("A passcode has not been set")
                            call.resolve([
                                "value": implementation.isAvailable("ERROR")
                            ])
                        
                        case LAError.Code.biometryNotAvailable.rawValue:
                            print("Biometric authentication not available")
                            call.resolve([
                                "value": implementation.isAvailable("ERROR")
                            ])
                        default:
                            print("Unknown error")
                            call.resolve([
                                "value": implementation.isAvailable("ERROR")
                            ])
                    }
                }
            }
    }
    
    @objc func verify(_ call: CAPPluginCall) {
        let defaults = UserDefaults.standard
        let key: String = call.getString("key") ?? ""
        let message: String = call.getString("message") ?? ""
        var context = LAContext()
        let reason = "Autenticación Biometrica"


        if((defaults.string(forKey: key)) != nil){
            print("verificamos la key")
            context.evaluatePolicy(.deviceOwnerAuthentication, localizedReason: reason) { [self] (success, evaluateError) in
                if success {
                    call.resolve([
                        "value": implementation.verify("OK")
                    ])
                }else{
                    call.resolve([
                        "value": implementation.verify("ERROR")
                    ])
                }
                
            }
            
        }else{
            print("No guardamos la key")
            call.resolve([
                "value": implementation.verify("ERROR")
            ])
        }
    }
    
    @objc func save(_ call: CAPPluginCall) {
        let defaults = UserDefaults.standard
        let key: String = call.getString("key") ?? ""
        let password: String = call.getString("password") ?? ""
        var context = LAContext()
        let reason = "Autenticación Biometrica"

        
        defaults.set(password, forKey: key)

        if((defaults.string(forKey: key)) != nil){
            print("guardamos la key")
            context.evaluatePolicy(.deviceOwnerAuthentication, localizedReason: reason) { [self] (success, evaluateError) in
                if success {
                    call.resolve([
                        "value": implementation.save("OK")
                    ])
                }else{
                    call.resolve([
                        "value": implementation.save("ERROR")
                    ])
                }
                
            }
            
        }else{
            print("No guardamos la key")
            call.resolve([
                "value": implementation.save("ERROR")
            ])
        }
    }
    
     @objc func saveUser(_ call: CAPPluginCall) {
        let defaults = UserDefaults.standard
        let user: String = call.getString("name") ?? "no hay nada"
        print("entramos en save user")
        
        defaults.set(user, forKey: "user")

        if((defaults.string(forKey: "user")) != nil){
            print("guardamos el usuario")
                
                    call.resolve([
                        "value": implementation.saveUser("OK")
                    ])
                }else{
                    call.resolve([
                        "value": implementation.saveUser("ERROR")
                    ])
                }
            
    }

     @objc func getUser(_ call: CAPPluginCall) {
        let userDefaults = UserDefaults.standard
         let name: String = userDefaults.string(forKey: "user") ?? ""
         
         print("estamos en getUser")


        if((userDefaults.string(forKey: "user")) != nil){
            print("Hay usuario")
            call.resolve([
                "value": implementation.getUser("hay user")
            ])
        }else{
            print("No hay usuario")
            call.resolve([
                "value": implementation.getUser("ERROR")
            ])
        }
        

        print("imprimimos \(name)")
        call.resolve([
            "value": implementation.has(name)
        ])
    }

    @objc func cleanAll(_ call: CAPPluginCall) {
        let defaults = UserDefaults.standard
        let dictionary = defaults.dictionaryRepresentation()
        dictionary.keys.forEach { key in
        defaults.removeObject(forKey: key)
    }
            call.resolve([
                "value": implementation.cleanAll("OK")
            ])
    }
    
}

extension LAContext {
    static var savedBiometricsPolicyState: Data? {
        get {
            UserDefaults.standard.data(forKey: "BiometricsPolicyState")
        }
        set {
            UserDefaults.standard.set(newValue, forKey: "BiometricsPolicyState")
        }
    }
    
    static func biometricsChanged() -> Bool {
        let context = LAContext()
        var error: NSError?
        context.canEvaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, error: &error)
        
        // If there is no saved policy state yet, save it
        if error == nil && LAContext.savedBiometricsPolicyState == nil {
            LAContext.savedBiometricsPolicyState = context.evaluatedPolicyDomainState
            return false
        }
        
        if let domainState = context.evaluatedPolicyDomainState, domainState != LAContext.savedBiometricsPolicyState {
            return true
        }
        
        return false
    }
}
