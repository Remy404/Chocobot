FROM openjdk:22
#COPY pom.xml /tmp/
#COPY src /tmp/src/
WORKDIR /tmp/
EXPOSE 8080
COPY target/MyTodoList-0.0.1-SNAPSHOT.jar MyTodoList.jar
RUN mkdir -p /mtdrworkshop2/creds
COPY Wallet_reacttodo10lise /mtdrworkshop2/creds
ENV db_user="TODOUSER"
#ENV ui_username="admin"
#ENV ui_password="EraParaAyer23!"
ENV dbpassword="Chocolabs2024"
ENV db_url="jdbc:oracle:thin:@reacttodo10lise_tp?TNS_ADMIN=/mtdrworkshop2/creds"
ENV driver_class_name="oracle.jdbc.OracleDriver"
ENV _JAVA_OPTIONS=-Dlogging.level.org.springframework=INFO
ENTRYPOINT ["java","-jar","MyTodoList.jar"]
