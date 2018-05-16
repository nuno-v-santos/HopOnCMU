CERT_PATH=/mnt/c/Users/tiago/Documents/CMU/servercmu/src/
CERT_ALIAS=mydomain_letsencrypt
PKCS_FILENAME=pkcs.p12
PKCS_PW='mestrado'
KEYSTORE_FILENAME=keystore.jks
KEYSTORE_PW='mestrado'

# generate PKCS12 file
openssl pkcs12 -export -in $CERT_PATH/cert.pem -inkey $CERT_PATH/key.pem -out $PKCS_FILENAME -name $CERT_ALIAS -passout pass:$PKCS_PW

# delete existing entry in Java keystore
keytool -delete -keystore $KEYSTORE_FILENAME -alias $CERT_ALIAS -storepass $KEYSTORE_PW

# add new Java keystore entry from PKCS12 file
keytool -importkeystore -deststorepass $KEYSTORE_PW -destkeypass $KEYSTORE_PW -destkeystore $KEYSTORE_FILENAME -srckeystore $PKCS_FILENAME -srcstoretype PKCS12 -srcstorepass $PKCS_PW -alias $CERT_ALIAS

rm $PKCS_FILENAME