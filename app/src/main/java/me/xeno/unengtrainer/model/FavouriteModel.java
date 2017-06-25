package me.xeno.unengtrainer.model;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.greendao.gen.FavouriteRecordDao;
import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.model.entity.FavouriteWrapper;

/**
 * Created by Administrator on 2017/6/25.
 */

public class FavouriteModel {

    public Observable<FavouriteWrapper> loadFavourite() {
        return Observable.create(new ObservableOnSubscribe<FavouriteWrapper>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<FavouriteWrapper> e) throws Exception {
                e.onNext(getFavouriteFromDb());
                e.onComplete();
            }
        });
    }

    private FavouriteWrapper getFavouriteFromDb() {
        FavouriteRecordDao mDao = DataManager.getInstance().getDaoSession().getFavouriteRecordDao();
        List<FavouriteRecord> records = mDao.loadAll();
        FavouriteWrapper wrapper = new FavouriteWrapper();
        wrapper.setDataList(records);
        return wrapper;
    }
}
