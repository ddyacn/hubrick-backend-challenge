package org.challenge.data.reader;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;

import org.challenge.data.model.Employee;
import org.challenge.data.model.EmployeeAge;
import org.junit.Assert;
import org.junit.Test;

public class MergedEmployeeDataReaderTest {

  @Test
  public void testThatDataReaderCombinesDataFrom3Sources() {

    DataReader<Employee> reader = new MergedEmployeeDataReader(
        () -> singletonList(Employee.of("June Holland", "f", 1, 2670.00)),
        () -> singletonList(EmployeeAge.of("June Holland", 49)),
        () -> asList("Accounting", "Sales")
    );

    Employee employee = reader.read().get(0);

    Assert.assertThat(employee.getName(), is("June Holland"));
    Assert.assertThat(employee.getGender(), is("f"));
    Assert.assertThat(employee.getDepartmentId(), is(1));
    Assert.assertThat(employee.getDepartmentName(), is("Accounting"));
    Assert.assertThat(employee.getSalaryDouble(), is(2670.00));
    Assert.assertThat(employee.getAge(), is(49));
    Assert.assertThat(employee.getAgeDouble(), is(49.00));
  }

  @Test
  public void testThatUnknownDepartmentIsMappedToAutoGeneratedName() {

    int unknownDepartmentId = 7;

    DataReader<Employee> reader = new MergedEmployeeDataReader(
        () -> singletonList(Employee.of("June Holland", "f", unknownDepartmentId, 2670.00)),
        () -> singletonList(EmployeeAge.of("June Holland", 49)),
        () -> asList("Accounting", "Sales")
    );

    Employee employee = reader.read().get(0);

    Assert.assertThat(employee.getDepartmentId(), is(7));
    Assert.assertThat(employee.getDepartmentName(), is("unknown_department_7"));
  }
}
