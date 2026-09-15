Technologies used
- 
- IntelliJ IDEA
- GitHub
- Trello
- Draw.io

Link to Trello
- 
https://trello.com/invite/b/6a9fa95888daf33b318caa49/ATTI54f8c482afef030c9c56fd22cd4a40151119BBE3/ga-bankingwithjavaproject

Link to Additional Resources
- 
- https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
- https://medium.com/@YodgorbekKomilo/understanding-hashing-in-java-a-guide-with-examples-e8c9e86a9371
- https://www.xe.com/currencyconverter/
- https://github.com/Java-FT-01-Bahrain/JDB-Info/tree/main/Lessons/java-tdd

Planning, Development Process & Problem-solving strategy
-
A list of UserStories was created in Trello, which then helped in forming sub tasks that would structure the project outline. Using Trello, I created a board that included 5 sections (ToDo, In Progress, Completed, Tested, and Github pushed) these sections had the tasks which included sub tasks and deadline due date attached to them, helping in achieving the final goal of the project while being on track with the progress. 
The project was built incrementally, feature by feature. It started with core project structure like Customer, Account, Bank and simple file persistence to represent a database, then hashing password, login, and account creation was integrated, followed by different types of transactions, overdrafts, card tiers and daily limits. Then, transactions, filtering, fraud detection, bank role in accepting accounts, and currency conversions. 
Each feature was added and tested manually against the console, a Unit Testing was then created for some of the Account.java methods to test their use.

Unresolved Issues / Future Improvements
- 
- Multiple Master Cards: customers are allowed to upgrade their card but never downgrade. In addition, customers can only upgrade their card but not have multiple (or a maximum of 3). As a future improvement, a logic that would enhance master card management would be integrated. 
- Banker Approval: the banker is only allowed to approve a customer, but never rejects with a reason. As a future improvement, a banker reject logic can be integrated.
- Notifications: the banker approves the customer account, but the customer is never notified unless logged in to attempt a transaction. As a future improvement, a notification logic system can be integrated to notify customers of needed alerts and notifications.

Favorite Function
- 

ERD
- 
<img width="932" height="912" alt="GA_Bank_Project drawio" src="https://github.com/user-attachments/assets/90e25992-4cff-43a6-969b-731f4951fba3" />
