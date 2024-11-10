package pl.danielstrielnikow.carscraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;

public class VehicleDataChecker {
    private static final String REGISTRATION_NUMBER = "SBL73692";
    private static final String VIN = "ZFA31200000149863";
    private static final int YEAR = 2009;
    public static void main(String[] args) {
        // Ścieżka do sterownika przeglądarki (np. chromedriver.exe)
        System.setProperty("webdriver.chrome.driver", "/Users/daniel/.cache/selenium/chromedriver/mac-arm64/130.0.6723.116/chromedriver");

        // Tworzenie instancji przeglądarki
        WebDriver driver = new ChromeDriver();

        try {
            // Otwórz stronę z formularzem
            driver.get("https://historiapojazdu.gov.pl/strona-glowna");

            // Wypełnij formularz


            // Przygotowanie formatu daty (dd.mm.rrrr)
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.set(YEAR, Calendar.JANUARY, 1); //

            boolean found = false;

            // Przechodź po wszystkich dniach 1999 roku
            while (!found) {
                WebElement registrationNumber = driver.findElement(By.id("_historiapojazduportlet_WAR_historiapojazduportlet_:rej"));
                WebElement vin = driver.findElement(By.id("_historiapojazduportlet_WAR_historiapojazduportlet_:vin"));

                // Podaj dane (zastąp poniższe wartości swoimi)
                registrationNumber.click();
                registrationNumber.sendKeys(REGISTRATION_NUMBER);
                vin.click();
                vin.sendKeys(VIN);
                String formattedDate = sdf.format(calendar.getTime());
                WebElement registrationDate = driver.findElement(By.id("_historiapojazduportlet_WAR_historiapojazduportlet_:data"));
                registrationDate.clear();
                // Kliknij w pole daty, aby wymusić walidację
                registrationDate.click();
                registrationDate.sendKeys(formattedDate); // Wprowadź datę

                // Kliknij przycisk 'Sprawdź pojazd'
                WebElement submitButton = driver.findElement(By.id("_historiapojazduportlet_WAR_historiapojazduportlet_:btnSprawdz"));
                submitButton.click();

                // Czekaj na załadowanie strony wynikowej
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                try {
                    // Czekaj na obecność elementu z wynikami pojazdu
                    WebElement vehicleDataElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.id("_historiapojazduportlet_WAR_historiapojazduportlet_:j_idt10")));

                    if (vehicleDataElement != null) {
                        found = true;
                        System.out.println("Znaleziono dane dla daty: " + formattedDate);
                    }

                } catch (Exception e) {
                    // Jeśli nie znaleziono danych, kontynuuj szukanie
                    System.out.println("Brak danych dla daty: " + formattedDate);
                }

                // Przejdź do następnego dnia
                calendar.add(Calendar.DAY_OF_YEAR, 1);  // Przejdź do następnego dnia
            }
        } catch (Exception e) {
            System.out.println("Wystąpił błąd: " + e.getMessage());
        }
    }
}
