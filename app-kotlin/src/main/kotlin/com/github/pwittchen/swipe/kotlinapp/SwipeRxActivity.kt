/*
 * Copyright (C) 2016 Piotr Wittchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pwittchen.swipe.kotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import com.github.pwittchen.swipe.library.Swipe
import kotlinx.android.synthetic.main.activity_main.info
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SwipeRxActivity : AppCompatActivity() {

  private var swipe: Swipe? = null
  private var subscription: Subscription? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    swipe = Swipe()
    subscription = (swipe as Swipe).observe()
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { swipeEvent ->
          info.text = swipeEvent.toString()
        }
  }

  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    (swipe as Swipe).dispatchTouchEvent(event)
    return super.dispatchTouchEvent(event)
  }

  override fun onPause() {
    super.onPause()
    safelyUnsubscribe(subscription as Subscription);
  }

  fun safelyUnsubscribe(subscription: Subscription) {
    if (subscription != null && !subscription.isUnsubscribed) {
      subscription.unsubscribe()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.listener -> onBackPressed()
      R.id.rx -> {
      }
    }
    return true
  }
}
