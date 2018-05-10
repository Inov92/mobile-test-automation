package com.epam.appium.pageobjects;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;

public class MainScreen {

    @AndroidFindBy(id = "com.epam.hello:id/editText")
    private MobileElement editTextField;

    @AndroidFindBy(id = "com.epam.hello:id/okButton")
    private MobileElement okButton;

    @AndroidFindBy(id = "com.epam.hello:id/helloText")
    private MobileElement helloText;

    @AndroidFindBy(id = "android:id/message")
    private MobileElement alertText;

    public MainScreen(AndroidDriver<?> driver){
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void fillTextField(String sText){
        editTextField.click();
        editTextField.sendKeys(sText);
    }

    public void pressOkButton(){
        okButton.click();
    }

    public String getHelloText(){
        return helloText.getText();
    }

    public void clearEditText(){
        editTextField.clear();
    }

    public String getAlertText(){
        return alertText.getText();
    }

}
