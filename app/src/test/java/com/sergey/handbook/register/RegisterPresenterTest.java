package com.sergey.handbook.register;

import android.content.Context;

import com.sergey.handbook.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Sergey.
 */
@RunWith(MockitoJUnitRunner.class)
public class RegisterPresenterTest {
    private RegisterPresenter presenter;

    @Mock
    private RegisterView view;

    private RegisterService service;

    @Mock
    private Context context;

    @Before
    public void setUp() throws Exception {
        service = new RegisterService(context);
        presenter = new RegisterPresenter(view, service);
    }

    @Test
    public void testShouldShowErrorMessageWhenPhoneIsEmpty() throws Exception {
        when(view.getPhoneNumber()).thenReturn("");
        presenter.onRegisterClicked();

        verify(view).showPhoneNumberError(R.string.phonenumber_empty_error);
    }

    @Test
    public void testShoudlShowErrorMessageWhenPhoneIsIncorrect() throws Exception {
        when(view.getPhoneNumber()).thenReturn("88005553535");
        when(service.isPhoneNumberCorrect("88005553535")).thenReturn(false);
        presenter.onRegisterClicked();

        verify(view).showPhoneNumberError(R.string.phonenumber_incorrect_error);
    }

    @Test
    public void testShouldShowErrorMessageIfPhoneIsNotInDB() throws Exception {
        when(view.getPhoneNumber()).thenReturn("89505439888");
        //when(service.isPhoneNumberCorrect("89505439888")).thenReturn(true);
        presenter.onRegisterClicked();

        verify(view).showPhoneNumberError(R.string.phonenumber_notin_DB);
    }
}