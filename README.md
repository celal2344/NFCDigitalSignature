Simple NFC/HCE application for my lecture project that encrypts a string and sends it to the other application along with a list of other random strings, after sending the encrypted string application sends the public key to decrypt the signature and highlight the correct string on the list.

Used java security library for the encryption and decryption processes. 

Host-based card emulation for NFC data transmission. Applications communicate through NFC 2 times so there is 2 different APDU commands for each communication process.

App1:

<img src="https://github.com/celal2344/NFCDigitalSignature/assets/69896844/16770532-9934-481d-999c-2c7c69d3cb90" height="500">
<img src="https://github.com/celal2344/NFCDigitalSignature/assets/69896844/69bc6763-ff8b-4de5-8ef4-ae010e4946cc" height="500">


App2:

<img src="https://github.com/celal2344/NFCDigitalSignature/assets/69896844/56fb066f-0990-49fb-ba28-2321123eff6f" height="500">
<img src="https://github.com/celal2344/NFCDigitalSignature/assets/69896844/07b2b0b6-e5b5-4748-a9e7-3321dd9d7f56" height="500">


References:

https://www.geeksforgeeks.org/rsa-and-digital-signatures/

https://medium.com/the-almanac/how-to-build-a-simple-smart-card-emulator-reader-for-android-7975fae4040f

