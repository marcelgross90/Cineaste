package de.cineaste.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.cineaste.android.entity.Season;

public class SeasonDao extends BaseDao {
    private final SimpleDateFormat sdf;
    private static SeasonDao instance;

    private SeasonDao(Context context) {
        super(context);
        sdf = new SimpleDateFormat("yyyy-MM-dd", context.getResources().getConfiguration().locale);
    }

    public static SeasonDao getInstance(Context context) {
        if (instance == null) {
            instance = new SeasonDao(context);
        }

        return instance;
    }

    void create(Season season, long seriesId) {
        if (season.getSeasonNumber() == 0) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(SeasonEntry._ID, season.getId());
        if (season.getReleaseDate() == null) {
            values.put(SeasonEntry.COLUMN_SEASON_RELEASE_DATE, "");
        } else {
            values.put(SeasonEntry.COLUMN_SEASON_RELEASE_DATE, sdf.format(season.getReleaseDate()));
        }
        values.put(SeasonEntry.COLUMN_SEASON_EPISODE_COUNT, season.getEpisodeCount());
        values.put(SeasonEntry.COLUMN_SEASON_POSTER_PATH, season.getPosterPath());
        values.put(SeasonEntry.COLUMN_SEASON_SEASON_NUMBER, season.getSeasonNumber());
        values.put(SeasonEntry.COLUMN_SEASON_SERIES_ID, seriesId);

        writeDb.insert(SeasonEntry.TABLE_NAME, null, values);
    }

    List<Season> read(String selection, String[] selectionArgs) {
        List<Season> seasons = new ArrayList<>();

        String[] projection = {
                SeasonEntry._ID,
                SeasonEntry.COLUMN_SEASON_RELEASE_DATE,
                SeasonEntry.COLUMN_SEASON_EPISODE_COUNT,
                SeasonEntry.COLUMN_SEASON_POSTER_PATH,
                SeasonEntry.COLUMN_SEASON_SEASON_NUMBER,
                SeasonEntry.COLUMN_SEASON_SERIES_ID
        };

        Cursor c = readDb.query(
                SeasonEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                SeasonEntry.COLUMN_SEASON_SEASON_NUMBER + " ASC",
                null);

        if (c.moveToFirst()) {
            do {
                Season currentSeason = new Season();
                currentSeason.setId(
                        c.getLong(c.getColumnIndexOrThrow(SeasonEntry._ID)));
                try {
                    currentSeason.setReleaseDate(sdf.parse(c.getString(c.getColumnIndexOrThrow(SeasonEntry.COLUMN_SEASON_RELEASE_DATE))));
                } catch (ParseException ex) {
                    currentSeason.setReleaseDate(null);
                }
                currentSeason.setEpisodeCount(c.getInt(c.getColumnIndexOrThrow(SeasonEntry.COLUMN_SEASON_EPISODE_COUNT)));
                currentSeason.setPosterPath(c.getString(c.getColumnIndexOrThrow(SeasonEntry.COLUMN_SEASON_POSTER_PATH)));
                currentSeason.setSeasonNumber(c.getInt(c.getColumnIndexOrThrow(SeasonEntry.COLUMN_SEASON_SEASON_NUMBER)));
                currentSeason.setSeriesId(c.getLong(c.getColumnIndexOrThrow(SeasonEntry.COLUMN_SEASON_SERIES_ID)));

                seasons.add(currentSeason);
            } while (c.moveToNext());
        }
        c.close();
        return seasons;
    }

    void update(Season season) {
        if (season.getSeasonNumber() == 0) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(BaseDao.SeasonEntry._ID, season.getId());

        if (season.getReleaseDate() == null) {
            values.put(SeasonEntry.COLUMN_SEASON_RELEASE_DATE, "");
        } else {
            values.put(SeasonEntry.COLUMN_SEASON_RELEASE_DATE, sdf.format(season.getReleaseDate()));
        }
        values.put(BaseDao.SeasonEntry.COLUMN_SEASON_EPISODE_COUNT, season.getEpisodeCount());
        values.put(BaseDao.SeasonEntry.COLUMN_SEASON_POSTER_PATH, season.getPosterPath());
        values.put(BaseDao.SeasonEntry.COLUMN_SEASON_SEASON_NUMBER, season.getSeasonNumber());
        values.put(BaseDao.SeasonEntry.COLUMN_SEASON_SERIES_ID, season.getSeriesId());

        String selection = BaseDao.SeasonEntry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(season.getId())};

        writeDb.update(SeasonEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    void delete(long id) {
        writeDb.delete(SeasonEntry.TABLE_NAME, SeasonEntry._ID + " = ?", new String[]{id + ""});
    }

    void deleteBySeriesId(long id) {
        writeDb.delete(SeasonEntry.TABLE_NAME, SeasonEntry.COLUMN_SEASON_SERIES_ID + " = ?", new String[]{id + ""});
    }
}
