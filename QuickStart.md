
## Prerequisites
* jdk 11
* maven (3.8.1 or later)
## Notes
* Entity Diagram:
>### PowerOfAttorneyEntity --(n)-------(1)-- AccountEntity
* For further improvement CustomerEntity can be introduced. the CustomerEntity can be used instead of  the GrantorName,GranteeName fields of PowerOfAttorneyEntity and AccountHolderName field of AccountEntity.
* The Opensource library Mapstruct can be used to handle the boilerplate bean mapping code.

## How to start

Step #1:clone repository from github
> https://github.com/RajuTalisetty/RabobankAssignment.git

Step #2: switch to local git repository directory

> cd RabobankAssignment/

Step #3: checkout the "assignment-alper-turkyilmaz" branch

> git checkout assignment-alper-turkyilmaz/

Step #4: run maven build/install

> mvn -f ./pom.xml clean install

Step #5: run the spring boot application

> java -jar ./api/target/rabobank-assignment-api-0.0.1-SNAPSHOT.jar


Step #4: test
- (a) create accounts using POST "/account/" endpoint. Example payload:
> {
  "accountType":"SAVINGS",
  "accountHolderName":"AccountHolder-1",
  "balance":100.0
}
- (b) give authorization to  grantee ("Grantee-1" in this case) by calling POST "/poa/" endpoint. Example payload:
 The accountNumber must be set to the accountNumber value from the response of step (a) 
  > {
  "granteeName":"Grantee-1",
  "grantorName":"AccountHolder-1",
  "accountNumber":"79df98bb-397c-4f1f-a9e4-95522c2e86bb",
  "authorization":"READ"
  }




