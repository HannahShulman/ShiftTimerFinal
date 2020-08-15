//package com.shift.timer
//
//class LiveDataCallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<ApiResponse<R>>> {
//
//    override fun responseType(): Type {
//        return responseType
//    }
//
//    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
//        return object : LiveData<ApiResponse<R>>() {
//            var started = AtomicBoolean(false)
//
//            override fun onActive() {
//                super.onActive()
//                if (started.compareAndSet(false, true)) {
//                    call.enqueue(object : Callback<R> {
//                        override fun onResponse(call: Call<R>, response: Response<R>) {
//                            postValue(ApiResponse.create(response))
//                        }
//
//                        override fun onFailure(call: Call<R>, t: Throwable) {
//                            postValue(ApiResponse.create(t))
//                        }
//                    })
//                }
//            }
//        }
//    }
//}