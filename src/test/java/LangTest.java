package java;

import Servlet.LanguageTranslationServlet;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class LangTest{
  private LanguageTranslationServlet lang;
  
  @Before
  public void initializelangath(){
    lang = new LanguageTranslationServlet();
  }
  
  @Test(timeout=1000)
  public void inputShouldReturnCorrectTranslation() {
  assertEquals("'Hello' should be 'Hola' ", "Hola", lang.test());

  }
  
} 
