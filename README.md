# Attendance Management (by Hibernate)
Java course's project
## Environment
* IDE: IntelliJ IDEA Ultimate (educational license)
* Database: MySQL (XAMPP)
## How to setup
### Database
1. Install and open XAMPP, then start Apache and MySQL
2. Click Admin on MySQL
3. Open tab SQL at Home
4. Paste attached script and click GO
5. In IntelliJ, go to View - Tool Windows - Database
6. Click + and choose Data Source - MySQL
    * User: root
    * Database: attendance_management

### Hibernate
1. Right click on project - Add Framework Support...
2. Tick Hibernate and OK, 'Import Database Schema' window appears
   * Choose Data Source: attendance_management@localhost
   * Package: entity
   * Entity prefix/suffix is all empty 
   * Select all Database Schema
   * [x] Add to Session Factory
   * [ ] Generate Column Properties
   * [x] Tick Generate Separate XML per Entity
   * [ ] Generate JPA Annotations (Java5)
3. To reopen this above window:
   * Go to View - Tool Windows - Persistence
   * Right click `BTHibernate` - Generate Persistence Mapping - By Database Schema

NOTE: I move all ***.hbm.xml** files to `entity` package