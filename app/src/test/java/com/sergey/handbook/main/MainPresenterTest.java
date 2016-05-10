package com.sergey.handbook.main;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Created by Sergey.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {
    private MainPresenter presenter;

    @Mock
    private MainView view;

    @Mock
    private Context context;

    @Mock
    private MainService service;


    @Before
    public void setUp() throws Exception {
        presenter = new MainPresenter(view, service);
    }

    @Test
    public void testShouldReturnContactsList() throws Exception {
        //ContactsAdapter contactsAdapter = Mockito.mock(ContactsAdapter.class);
        //presenter.getContactsList();

    }

    @Test
    public void testShouldSetProgressBarInvisible() throws Exception {
        //presenter.getContactsList();

        //verify(view).setProgressBarVisibility(View.GONE);
    }
}