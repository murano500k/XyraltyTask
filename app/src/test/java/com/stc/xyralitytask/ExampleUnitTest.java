package com.stc.xyralitytask;

import org.junit.Test;

import java.util.List;

import io.reactivex.functions.BiConsumer;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = "ExampleUnitTest";
    private boolean flag=false;

    @Test
    public void addition_isCorrect() throws Exception {
        LoginPresenter presenter=new LoginPresenter();
        presenter.login("android.test@xyrality.com","password").subscribe(new BiConsumer<List<String>, Throwable>() {
            @Override
            public void accept(List<String> strings, Throwable throwable) throws Exception {
                System.err.println("test: "+strings);
                if(strings!=null) {
                    for (String s :
                            strings
                            ) {
                        System.out.println("s=" + s);
                    }
                }
                assertEquals(4, 2 + 2);
                flag=true;
            }
        });
        System.out.println("test");
        while(!flag){
            Thread.sleep(1000);
        }
        assertEquals(4, 2 + 2);
    }
}