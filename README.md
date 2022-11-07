# OAuth2 - Server
Microservicios con Spring Boot y Spring Cloud Netflix Eureka: Para complementar el contenido del libro: Microservicios, un enfoque integrado

# Importante
- Usaremos la versión de Spring Security OAuth2: **2.3.8.RELEASE**, es importante que sea esa versión, porque desde la 2.4 en adelante
se quita el componente que el Servidor de Authorización usa para generar el token y solamente se deja la parte del cliente.

- Si estamos usando java 8 no es necesario agregar la dependencia de **jaxb** porque ya viene incluida,
pero si usamos una versión superior, sí que es necesario agrega la dependencia.

- Excluimos la dependencia de Spring Data JPA ya que el servicio usuario-commons lo tiene, entonces para evitar que nos configure, lo excluimos desde este mismo xml. Recordar que otra forma sería excluyéndolo desde la misma clase principal de Spring Boot,tal como lo hicimos en el servicio items con la siguiente anotación: @EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})