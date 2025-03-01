package tech.ydb.yoj.repository.test.sample.model;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import tech.ydb.yoj.databind.schema.Column;
import tech.ydb.yoj.repository.db.Entity;
import tech.ydb.yoj.repository.db.Table;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Value
public class TypeFreak implements Entity<TypeFreak> {
    Id id;

    boolean primitiveBoolean;
    byte primitiveByte;
    @Column(dbType = "UINT8")
    byte primitiveByteUint8;
    short primitiveShort;
    int primitiveInt;
    long primitiveLong;
    float primitiveFloat;
    double primitiveDouble;
    Boolean boxedBoolean;
    Byte boxedByte;
    @Column(dbType = "UINT8")
    Byte boxedByteUint8;
    Short boxedShort;
    Integer boxedInteger;
    Long boxedLong;
    Float boxedFloat;
    Double boxedDouble;

    @Column(dbType = "UTF8")
    String utf8String;
    String string;
    byte[] bytes;
    Status status;
    @Column(dbType = "UTF8")
    Status utf8Status;
    @With
    Embedded embedded;
    @Column(flatten = false, dbType = "UTF8")
    Embedded utf8embedded;
    @Column(flatten = false, dbType = "STRING")
    Embedded stringEmbedded;
    @Column(flatten = false, dbType = "JSON_DOCUMENT")
    Embedded jsonDocumentEmbedded;
    @Column(flatten = false)
    Embedded jsonEmbedded;

    Instant instant;

    List<String> list1;
    List<Embedded> list2;
    Set<Embedded> list3;
    Map<Integer, Embedded> map1;
    @Column(dbType = "UTF8")
    Map<Integer, Embedded> utf8map;
    @Column(dbType = "STRING")
    Map<Integer, Embedded> stringMap;
    @Column(dbType = "JSON_DOCUMENT")
    Map<Integer, Embedded> jsonDocumentMap;

    StringValueWrapper stringValueWrapper;

    @Column(name = "custom_named_column")
    String customNamedColumn;

    @With
    Ticket ticket;

    @Value
    public static class Id implements Entity.Id<TypeFreak> {
        String first;
        int second;
    }

    @Value
    public static class Embedded {
        A a;
        B b;
    }

    @Value
    public static class A {
        String a;
    }

    @Value
    public static class B {
        String b;
    }

    public enum Status {
        DRAFT, OK
    }

    @Value
    public static class View implements Table.ViewId<TypeFreak> {
        Id id;
        Embedded embedded;
    }

    @Value
    public static class StringView implements Table.ViewId<TypeFreak> {
        Id id;
        String stringEmbedded;
    }

    public static final class StringValueWrapper {
        private final String value;

        public StringValueWrapper(String value) {
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof StringValueWrapper s && s.value.equals(value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * String-valued type with canonical static factory ({@link #valueOf(String)}) and a matching {@link #toString()}
     * instance method.
     * <p><em>E.g.,</em> {@code "XYZ-100500" <=> new Ticket(queue="XYZ", num=100500)}
     */
    public record Ticket(@NonNull String queue, int num) {
        public Ticket {
            Preconditions.checkArgument(num >= 1, "ticket number must be >= 1");
        }

        @NonNull
        public static Ticket valueOf(@NonNull String str) {
            int idx = str.indexOf('-');
            return new Ticket(str.substring(0, idx), Integer.parseInt(str.substring(idx + 1)));
        }

        @Override
        public String toString() {
            return queue + "-" + num;
        }
    }
}
