Pasos para Desplegar Localmente Backend (Spring Boot):
1. Clonar el repositorio: git clone https://github.com/juangrozo/btg-pactual-back.git
2. Las variables para las propiedades las validamos cuando estemos haciendo la demostracion y/o explicacion del codigo o
   pueden generar de manera gratuita las credenciales para Twilio y configurar la cuenta de Gmail:
   twilio.account_sid=YOUR_ACCOUNT_SID
   twilio.auth_token=YOUR_AUTH_TOKEN
   twilio.phone_number=YOUR_PHONE_NUMBRE_TWILIO
   spring.mail.username=YOUR_GMAIL
   spring.mail.password=YOUR_PASS_APPLICATION
3. Construir el proyecto: mvn clean install
4. Configurar el IDE para correr la aplicacion, en mi caso usé IntelliJ IDEA 2024.2 (Community Edition): spring-boot:run
5. La aplicacion corre por defecto en el puerto 8080
