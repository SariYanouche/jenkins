import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/Features",
        glue = "",
        plugin = {
                "json:build/reports/cucumber.json",
                "html:build/reports/cucumber/"
        }
)
public class ExampleTest {

}