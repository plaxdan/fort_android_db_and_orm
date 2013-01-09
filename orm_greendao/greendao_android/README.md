# greenDAO

- Takes a different approach by generating models and DAOs
- Has a limited set of "types" for generated model fields (i.e.: no UUID)
- Generated models are combination of DTO and Active Record design patterns
- Chained query builder syntax e.g.: List results = myModel.queryBuilder().where(Properties.FirstName.eq("Eric").OrderAsc(Properties.LastName);
- No support for "migrating" models