/*
        The MIT License (MIT)
        Copyright (c) 2015 King's College London

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/

package uk.ac.kcl.iop.brc.core.pipeline.common.utils;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ArrayUtilTest {

    @Test
    public void shouldReturnTrueIfStringArrayIsEmpty() {
        String[] array = new String[10];

        assertTrue(ArrayUtil.isStringArrayEmpty(array));
    }

    @Test
    public void shouldReturnFalseIfStringArrayIsNotEmpty() {
        String[] array = new String[10];
        array[0] = "test";
        assertFalse(ArrayUtil.isStringArrayEmpty(array));
    }

    @Test
    public void shouldReturnTrueIfStringArrayIsNull() {
        String[] array = null;

        assertTrue(ArrayUtil.isStringArrayEmpty(array));
    }

}
