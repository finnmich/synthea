package org.mitre.synthea.world.agents.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mitre.synthea.helpers.Config;
import org.mitre.synthea.world.agents.Payer;
import org.mitre.synthea.world.agents.Person;
import org.mitre.synthea.world.geography.Location;

public class PayerFinderTest {
  
  private Person person;

  /**
   * Setup for Payer Finder Tests.
   */
  @Before
  public void setup() {
    person = new Person(0L);
    person.attributes.put(Person.OCCUPATION_LEVEL, 0.5);
    person.attributes.put(Person.INCOME, 100);
    // Load in the .csv test list of payers.
    Config.set("generate.payers.insurance_companies.default_file",
        "generic/payers/test_payers.csv");
  }

  @Test
  public void noPayersRandom() {
    Config.set("generate.payers.selection_behavior", "random");
    Payer.clear();
    Payer.loadPayers(new Location("Massachusetts", null));
    PayerFinderRandom finder = new PayerFinderRandom();
    List<Payer> options = new ArrayList<Payer>();
    Payer payer = finder.find(options, person, null, 0L);
    assertEquals("NO_INSURANCE", payer.getName());
  }

  @Test
  public void onePayerRandom() {
    PayerFinderRandom finder = new PayerFinderRandom();
    List<Payer> options = new ArrayList<Payer>();
    options.add(Payer.getPrivatePayers().get(0));
    Payer payer = finder.find(options, person, null, 0L);
    assertNotNull(payer);
  }

  @Test
  public void noPayersBestRate() {
    Config.set("generate.payers.selection_behavior", "best_rate");
    Payer.clear();
    Payer.loadPayers(new Location("Massachusetts", null));
    PayerFinderBestRates finder = new PayerFinderBestRates();
    List<Payer> options = new ArrayList<Payer>();
    Payer payer = finder.find(options, person, null, 0L);
    assertEquals("NO_INSURANCE", payer.getName());
  }

  @Test
  public void onePayerBestRate() {
    Config.set("generate.payers.selection_behavior", "best_rate");
    Payer.clear();
    Payer.loadPayers(new Location("Massachusetts", null));
    PayerFinderBestRates finder = new PayerFinderBestRates();
    List<Payer> options = new ArrayList<Payer>();
    options.add(Payer.getPrivatePayers().get(0));
    Payer payer = finder.find(options, person, null, 0L);
    assertNotNull(payer);
  }

  @Test(expected = RuntimeException.class)
  public void invalidPayerFinderTest() {
    // Note that "bestrate" should be spelled "best_rate"
    Config.set("generate.payers.selection_behavior", "bestrate");
    Payer.clear();
    Payer.loadPayers(new Location("Massachusetts", null));
  }
}