package org.promgrammers.springbootbasic.domain.voucher.repository;

import org.promgrammers.springbootbasic.domain.voucher.model.Voucher;
import org.promgrammers.springbootbasic.exception.InvalidFilePathException;
import org.promgrammers.springbootbasic.exception.VoucherFileWriteException;
import org.promgrammers.springbootbasic.util.FileConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("dev")
@Primary
public class FileVoucherRepository implements VoucherRepository {

    private static final File filePath = new File("src/main/resources/storage/voucherStorage.txt");
    private static final Logger logger = LoggerFactory.getLogger(FileVoucherRepository.class);


    @Override
    public Voucher insert(Voucher voucher) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = FileConverter.voucherToLine(voucher);
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            logger.error("Invalid Input => {}", e.getMessage());
            throw new InvalidFilePathException("IO 시스템 오류입니다.");
        }
        return voucher;
    }

    @Override
    public Optional<Voucher> findById(UUID voucherId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Voucher voucher = FileConverter.parseVoucherFromLine(line);
                if (voucher.getVoucherId().equals(voucherId)) {
                    return Optional.of(voucher);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Voucher> findAll() {
        List<Voucher> vouchers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Voucher voucher = FileConverter.parseVoucherFromLine(line);
                vouchers.add(voucher);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vouchers;
    }

    @Override
    public void deleteAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new VoucherFileWriteException("Failed to delete all vouchers.");
        }
    }
}
