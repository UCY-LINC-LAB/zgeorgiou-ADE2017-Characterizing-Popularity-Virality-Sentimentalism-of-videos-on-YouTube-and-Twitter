package com.zgeorg03.services;

import com.mongodb.DBCollection;
import com.zgeorg03.database.DBServices;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public abstract class Service {

    protected final DBServices dbServices;

    public Service(DBServices dbServices) {
        this.dbServices =dbServices;
    }
}
