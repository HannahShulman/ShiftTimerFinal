package com.shift.timer.db;

import com.shift.timer.model.Shift;
import com.shift.timer.model.Workplace;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Created by roy on 5/31/2017.
 */
@Database(entities = {Shift.class, Workplace.class}, version = 1)

@TypeConverters({Converters.class})
public abstract class AppDB extends RoomDatabase {

    public abstract ShiftDao shiftDao();

    public abstract WorkplaceDao workplaceDao();
}
