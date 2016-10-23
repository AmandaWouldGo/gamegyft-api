


openssl-keystore:
	openssl pkcs12 -export -in cert.pem -inkey "private_key.pem" -certfile cert.pem -out keystore.p12




java-keystore:
	-rm keystore.jks
	openssl pkcs12 -export -in cert.pem -inkey "private_key.pem" -certfile cert.pem -out keystore.p12
	keytool -importkeystore -srckeystore keystore.p12 -srcstoretype PKCS12 -destkeystore keystore.jks
	-rm keystore.p12
	keytool -import -alias ejbca -keystore keystore.jks -file VDPCA-Sandbox.pem -storepass password

