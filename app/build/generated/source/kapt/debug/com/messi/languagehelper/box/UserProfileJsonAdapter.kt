// Code generated by moshi-kotlin-codegen. Do not edit.
package com.messi.languagehelper.box

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.internal.Util
import java.lang.NullPointerException
import java.lang.reflect.Constructor
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.emptySet
import kotlin.jvm.Volatile
import kotlin.text.buildString

@Suppress("DEPRECATION", "unused", "ClassName", "REDUNDANT_PROJECTION")
class UserProfileJsonAdapter(
  moshi: Moshi
) : JsonAdapter<UserProfile>() {
  private val options: JsonReader.Options = JsonReader.Options.of("id", "user_name", "user_img",
      "credits", "check_in_sum", "last_check_in", "course_score", "course_unit_sum",
      "course_level_sum", "backkup", "backkup1", "backkup2", "backkup3", "backkup4", "backkup5")

  private val longAdapter: JsonAdapter<Long> = moshi.adapter(Long::class.java, emptySet(), "id")

  private val stringAdapter: JsonAdapter<String> = moshi.adapter(String::class.java, emptySet(),
      "user_name")

  private val intAdapter: JsonAdapter<Int> = moshi.adapter(Int::class.java, emptySet(), "credits")

  private val nullableStringAdapter: JsonAdapter<String?> = moshi.adapter(String::class.java,
      emptySet(), "backkup")

  @Volatile
  private var constructorRef: Constructor<UserProfile>? = null

  override fun toString(): String = buildString(33) {
      append("GeneratedJsonAdapter(").append("UserProfile").append(')') }

  override fun fromJson(reader: JsonReader): UserProfile {
    var id: Long? = 0L
    var user_name: String? = null
    var user_img: String? = null
    var credits: Int? = 0
    var check_in_sum: Int? = 0
    var last_check_in: String? = null
    var course_score: Int? = 0
    var course_unit_sum: Int? = 0
    var course_level_sum: Int? = 0
    var backkup: String? = null
    var backkup1: String? = null
    var backkup2: String? = null
    var backkup3: String? = null
    var backkup4: String? = null
    var backkup5: String? = null
    var mask0 = -1
    reader.beginObject()
    while (reader.hasNext()) {
      when (reader.selectName(options)) {
        0 -> {
          id = longAdapter.fromJson(reader) ?: throw Util.unexpectedNull("id", "id", reader)
          // $mask = $mask and (1 shl 0).inv()
          mask0 = mask0 and 0xfffffffe.toInt()
        }
        1 -> {
          user_name = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("user_name",
              "user_name", reader)
          // $mask = $mask and (1 shl 1).inv()
          mask0 = mask0 and 0xfffffffd.toInt()
        }
        2 -> {
          user_img = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("user_img",
              "user_img", reader)
          // $mask = $mask and (1 shl 2).inv()
          mask0 = mask0 and 0xfffffffb.toInt()
        }
        3 -> {
          credits = intAdapter.fromJson(reader) ?: throw Util.unexpectedNull("credits", "credits",
              reader)
          // $mask = $mask and (1 shl 3).inv()
          mask0 = mask0 and 0xfffffff7.toInt()
        }
        4 -> {
          check_in_sum = intAdapter.fromJson(reader) ?: throw Util.unexpectedNull("check_in_sum",
              "check_in_sum", reader)
          // $mask = $mask and (1 shl 4).inv()
          mask0 = mask0 and 0xffffffef.toInt()
        }
        5 -> {
          last_check_in = stringAdapter.fromJson(reader) ?:
              throw Util.unexpectedNull("last_check_in", "last_check_in", reader)
          // $mask = $mask and (1 shl 5).inv()
          mask0 = mask0 and 0xffffffdf.toInt()
        }
        6 -> {
          course_score = intAdapter.fromJson(reader) ?: throw Util.unexpectedNull("course_score",
              "course_score", reader)
          // $mask = $mask and (1 shl 6).inv()
          mask0 = mask0 and 0xffffffbf.toInt()
        }
        7 -> {
          course_unit_sum = intAdapter.fromJson(reader) ?:
              throw Util.unexpectedNull("course_unit_sum", "course_unit_sum", reader)
          // $mask = $mask and (1 shl 7).inv()
          mask0 = mask0 and 0xffffff7f.toInt()
        }
        8 -> {
          course_level_sum = intAdapter.fromJson(reader) ?:
              throw Util.unexpectedNull("course_level_sum", "course_level_sum", reader)
          // $mask = $mask and (1 shl 8).inv()
          mask0 = mask0 and 0xfffffeff.toInt()
        }
        9 -> {
          backkup = nullableStringAdapter.fromJson(reader)
          // $mask = $mask and (1 shl 9).inv()
          mask0 = mask0 and 0xfffffdff.toInt()
        }
        10 -> {
          backkup1 = nullableStringAdapter.fromJson(reader)
          // $mask = $mask and (1 shl 10).inv()
          mask0 = mask0 and 0xfffffbff.toInt()
        }
        11 -> {
          backkup2 = nullableStringAdapter.fromJson(reader)
          // $mask = $mask and (1 shl 11).inv()
          mask0 = mask0 and 0xfffff7ff.toInt()
        }
        12 -> {
          backkup3 = nullableStringAdapter.fromJson(reader)
          // $mask = $mask and (1 shl 12).inv()
          mask0 = mask0 and 0xffffefff.toInt()
        }
        13 -> {
          backkup4 = nullableStringAdapter.fromJson(reader)
          // $mask = $mask and (1 shl 13).inv()
          mask0 = mask0 and 0xffffdfff.toInt()
        }
        14 -> {
          backkup5 = nullableStringAdapter.fromJson(reader)
          // $mask = $mask and (1 shl 14).inv()
          mask0 = mask0 and 0xffffbfff.toInt()
        }
        -1 -> {
          // Unknown name, skip it.
          reader.skipName()
          reader.skipValue()
        }
      }
    }
    reader.endObject()
    @Suppress("UNCHECKED_CAST")
    val localConstructor: Constructor<UserProfile> = this.constructorRef ?:
        UserProfile::class.java.getDeclaredConstructor(Long::class.javaPrimitiveType,
        String::class.java, String::class.java, Int::class.javaPrimitiveType,
        Int::class.javaPrimitiveType, String::class.java, Int::class.javaPrimitiveType,
        Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java,
        String::class.java, String::class.java, String::class.java, String::class.java,
        String::class.java, Int::class.javaPrimitiveType, Util.DEFAULT_CONSTRUCTOR_MARKER).also {
        this.constructorRef = it }
    return localConstructor.newInstance(
        id,
        user_name,
        user_img,
        credits,
        check_in_sum,
        last_check_in,
        course_score,
        course_unit_sum,
        course_level_sum,
        backkup,
        backkup1,
        backkup2,
        backkup3,
        backkup4,
        backkup5,
        mask0,
        null
    )
  }

  override fun toJson(writer: JsonWriter, value: UserProfile?) {
    if (value == null) {
      throw NullPointerException("value was null! Wrap in .nullSafe() to write nullable values.")
    }
    writer.beginObject()
    writer.name("id")
    longAdapter.toJson(writer, value.id)
    writer.name("user_name")
    stringAdapter.toJson(writer, value.user_name)
    writer.name("user_img")
    stringAdapter.toJson(writer, value.user_img)
    writer.name("credits")
    intAdapter.toJson(writer, value.credits)
    writer.name("check_in_sum")
    intAdapter.toJson(writer, value.check_in_sum)
    writer.name("last_check_in")
    stringAdapter.toJson(writer, value.last_check_in)
    writer.name("course_score")
    intAdapter.toJson(writer, value.course_score)
    writer.name("course_unit_sum")
    intAdapter.toJson(writer, value.course_unit_sum)
    writer.name("course_level_sum")
    intAdapter.toJson(writer, value.course_level_sum)
    writer.name("backkup")
    nullableStringAdapter.toJson(writer, value.backkup)
    writer.name("backkup1")
    nullableStringAdapter.toJson(writer, value.backkup1)
    writer.name("backkup2")
    nullableStringAdapter.toJson(writer, value.backkup2)
    writer.name("backkup3")
    nullableStringAdapter.toJson(writer, value.backkup3)
    writer.name("backkup4")
    nullableStringAdapter.toJson(writer, value.backkup4)
    writer.name("backkup5")
    nullableStringAdapter.toJson(writer, value.backkup5)
    writer.endObject()
  }
}
