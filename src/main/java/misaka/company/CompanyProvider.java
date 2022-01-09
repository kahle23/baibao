package misaka.company;

import java.util.List;

public interface CompanyProvider {

    /**
     * Obtain company information by company name.
     * @param name The company name
     * @return The company information
     */
    Company info(String name);

    /**
     * Search the list of companies by company name.
     * @param name The company name
     * @return The list of companies
     */
    List<Company> search(String name);

}
