package model_test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCharacterModel.class, TestEntityModel.class, TestGameData.class, 
	TestMenuModel.class, TestMyInput.class })
public class AllModelTests {

}
