package apiaggregator;

import client.ClientRequestGenerator;
import client.EndpointRequestGenerator;
import cvsprocessing.Student;
import json.APIResponseFormatter;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ApiAggregatorTest {

  @Test
  public void aggregateInfoTest() {
    EndpointRequestGenerator requestGenerator = new ClientRequestGenerator();
    APIResponseFormatter formatter = new APIResponseFormatter();

    ApiAggregator<Student> aggregator =
        new ApiAggregator<Student>(Type.Info, formatter, requestGenerator);
    List<Student> students = aggregator.aggregate();
    assertEquals(60, students.size());
  }

  @Test
  public void aggregateMatchTest() {
    EndpointRequestGenerator requestGenerator = new ClientRequestGenerator();
    APIResponseFormatter formatter = new APIResponseFormatter();

    ApiAggregator<Student> aggregator =
        new ApiAggregator<Student>(Type.Match, formatter, requestGenerator);
    List<Student> students = aggregator.aggregate();
    assertEquals(60, students.size());
  }

}