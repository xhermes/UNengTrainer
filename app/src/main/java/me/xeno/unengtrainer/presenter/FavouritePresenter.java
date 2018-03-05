package me.xeno.unengtrainer.presenter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.xeno.unengtrainer.model.FavouriteModel;
import me.xeno.unengtrainer.model.entity.FavouriteWrapper;
import me.xeno.unengtrainer.view.activity.FavouriteActivity;

/**
 * Created by Administrator on 2017/6/25.
 */

public class FavouritePresenter {

    private FavouriteModel mModel;
    private FavouriteActivity mActivity;

    private Disposable mLoadFavDisposable;

    public FavouritePresenter(FavouriteActivity activity) {
        this.mActivity = activity;
        mModel = new FavouriteModel();
    }

    public void loadFavourites() {
        mModel.loadFavourite()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FavouriteWrapper>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLoadFavDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull FavouriteWrapper favouriteWrapper) {
                        mActivity.updateRecycler(favouriteWrapper.getDataList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteFavouriteFromDb(long id) {
        mModel.deleteFavouriteFromDb(id);
    }

    public void onDestroy() {
        if(mLoadFavDisposable != null && !mLoadFavDisposable.isDisposed())
            mLoadFavDisposable.dispose();
    }
}
