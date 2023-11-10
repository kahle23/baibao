package misaka.hr;

import misaka.hr.employee.EmployeeOperations;
import misaka.hr.employee.EmploymentOperations;
import misaka.hr.employee.OrganizationOperations;
import misaka.hr.employee.PositionOperations;

/**
 * Human resources employee template
 * @see <a href="https://en.wikipedia.org/wiki/Human_resource_management_system">Human resource management system</a>
 * @see <a href="https://en.wikipedia.org/wiki/Staff_management">Staff management</a>
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/27 Deletable
public interface HrEmployeeTemplate {

    /**
     * Operate organization related data.
     * @return The organization related operation object
     */
    OrganizationOperations opsForOrganization();

    /**
     * Operate position related data.
     * @return The position related operation object
     */
    PositionOperations opsForPosition();

    /**
     * Operate employment related data.
     * @return The employment related operation object
     */
    EmploymentOperations opsForEmployment();

    /**
     * Operate employee related data.
     * @return The employee related operation object
     */
    EmployeeOperations opsForEmployee();

}
