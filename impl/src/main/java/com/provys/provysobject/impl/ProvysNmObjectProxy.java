package com.provys.provysobject.impl;

import com.provys.provysobject.ProvysNmObject;

interface ProvysNmObjectProxy<O extends ProvysNmObject, V extends ProvysNmObjectValue>
    extends ProvysObjectProxy<O, V>, ProvysNmObject {

}
