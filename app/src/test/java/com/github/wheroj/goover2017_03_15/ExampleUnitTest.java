package com.github.wheroj.goover2017_03_15;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getName(){
        assertEquals("com.github.com.wheroj.goover2017_03_15", CommonUtils.getPackageName(), null);
    }

    @Test
    public void login(){
        MainActivity activity = mock(MainActivity.class);

        ArrayList<String> list = mock(ArrayList.class);
        Mockito.when(list.get(0)).thenReturn("hahah");

//        verify(list).add("yyyyy");
//        verify(list).clear();
        System.out.println(list.get(0));
        assertEquals("com.github.com.wheroj.goover2017_03_15", activity.getPackageName(), null);

        SPUtils.setInt("count", 2);
        verify(activity).getHistory();
    }
}