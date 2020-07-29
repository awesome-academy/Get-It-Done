## Related Tickets
- [#Ticket ID](https://dev.framgia.com/redmine/issues/???)

## WHAT
- Change number items `completed/total` in admin page.

## HOW
- I edit js file, inject not_vary_normal items in calculate function.

## WHY
- Because in previous version - number just depends on `normal` items. But in new version, we have `state` and `confirm_state` depends on both `normal` + `not_normal` items.

## Evidence (Screenshot or Video)

## Performance (Optional)
- [ ] Resolved n + 1 query

- [ ] Time run rake task : 1000 ms

- Generated SQL query

## Review Checklist

Category | View Point | Description | Expected Reviewer Answer | Reviewer1 (name) | Reviewer2 (name)
--- | --- | --- | --- | --- | ---
General |Code should be self-explanatory. | Get a feel of story reading, while going through the code. Use appropriate name for variables, functions and classes. If you are taking more time to understand the code, then either code needs refactoring or at least comments have to be written to make it clear. | YES |<li>- [ ] yes</li>|<li>- [ ] yes</li>  
General |Is this function or class too big? If yes, is the function or class having too many responsibilities? | | NO |<li>- [ ] no</li>|<li>- [ ] no</li>  
General | Does the code follow DRY?: Do not Repeat Yourself | Is there any redundant or duplicate code? DRY (Do not Repeat Yourself) principle: The same code should not be repeated more than twice. Consider reusable services, functions and components. Consider generic functions and classes. | YES |<li>- [ ] yes</li>|<li>- [ ] yes</li>  
General |Exception handling and cleanup (dispose) resources | | YES |<li>- [ ] yes</li>|<li>- [ ] yes</li>  
General | Does the code build and execute correctly? | | YES |<li>- [ ] yes</li>|<li>- [ ] yes</li>  
Conventions | Does the source code have end-line errors? | | NO |<li>- [ ] no</li>|<li>- [ ] no</li>  
Conventions | Do the source code have end-line errors? | | NO |<li>- [ ] no</li>|<li>- [ ] no</li> 
Conventions | No hard coding, use constants/configuration values | | YES |<li>- [ ] yes</li>|<li>- [ ] yes</li>  
Conventions | Does the code follow Sun* coding style and coding conventions? | https://github.com/framgia/coding-standards/tree/master/eng/android | YES |<li>- [ ] yes</li>|<li>- [ ] yes</li>  
Redmine | Does the ticket follow Sun* Redmine working process?  | https://github.com/framgia/Training-Guideline/blob/master/WorkingProcess/redmine/redmine.md| YES |<li>- [ ] yes</li>|<li>- [ ] yes</li>  
Documentation | Is there any incomplete code? If so, should it be removed or flagged with a suitable marker like ‘TODO’? |  | YES |<li>- [ ] yes</li>|<li>- [ ] yes</li>  

## Notes (Optional)
*(Impacted Areas in Application(List features, api, models or services that this PR will affect))*

*(List gem, library third party add new)*

*(Other notes)*
