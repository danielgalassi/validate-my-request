# validate-my-request
Just a personal project to validate data refresh requests at work
It includes:
- XML serialisation through native JAXB
- Drag n Drop feature to upload files in Chrome and Firefox
- Standard file upload UI for Internet Explorer
- Responsive CSS
- Ajax calls

###What sort of mistakes or inconsistencies are picked up by this app?
- missing objects (incorrect names, incorrect schema): if possible, the app will suggest the right schema
- incorrect object type: you thought it was a table... but nope... it was a synonym! If possible, the correct type will be suggested
- odd characters included in the request: as a result of copying & pasting 
- underlying objects for views not included in the request: views can't be created without those dependencies... we give you a warning and display those objects you may want to include in the request!

All of that in... less than one second... for real.

Javadoc available at https://danielgalassi.github.io/validate-my-request/


###Quick & Simple:

Drag and drop the refresh request (an Excel template)
![screenshot_2017-01-29_10-34-14](https://cloud.githubusercontent.com/assets/525272/22400718/f054b45e-e610-11e6-9855-170e3a771449.png)

Wait 1 second... and review the results
![screenshot_2017-01-29_10-37-00](https://cloud.githubusercontent.com/assets/525272/22400719/f05e0694-e610-11e6-8f27-fe189c41d5e1.png)
