package com.jjk.env_test.maintest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TreeMapTest {

	// Class representing an individual Ssdsh
	public static class Ssdsh {
		private final String shdate;
		private final String ampm;

		public Ssdsh(String shdate, String ampm) {
			this.shdate = shdate;
			this.ampm = ampm;
		}

		public String getShdate() {
			return shdate;
		}

		public String getAmpm() {
			return ampm;
		}

		@Override
		public String toString() {
			return "Ssdsh{" + "shdate='" + shdate + '\'' + ", ampm='" + ampm + '\'' + '}';
		}
	}

	// Class representing a single day's worth of Ssdsh objects
	public static class SingleDaySsdsh {
		private final LocalDate date;
		private final List<Ssdsh> ssdshList;

		public SingleDaySsdsh(Map.Entry<LocalDate, List<Ssdsh>> entry) {
			this.date = entry.getKey();
			this.ssdshList = entry.getValue();
		}

		public LocalDate getDate() {
			return date;
		}

		public List<Ssdsh> getSsdshList() {
			return ssdshList;
		}

		@Override
		public String toString() {
			return "SingleDaySsdsh{" + "date=" + date + ", ssdshList=" + ssdshList + '}';
		}
	}

	// Class that processes a list of Ssdsh objects
	public static class DaysSsdsh {
		private final List<SingleDaySsdsh> everyDaySsdshs;

		public static Comparator<Ssdsh> CUSTOM_SSDSH_COMPARATOR = Comparator
				.comparing(Ssdsh::getShdate, Comparator.comparing(LocalDate::parse)).thenComparing(Ssdsh::getAmpm);

		public DaysSsdsh(List<Ssdsh> ssdshes) {
			// (1.
			ssdshes.sort(CUSTOM_SSDSH_COMPARATOR);
			TreeMap<LocalDate, List<Ssdsh>> mapType = ssdshes.stream().collect(
					Collectors.groupingBy(s -> LocalDate.parse(s.getShdate()), TreeMap::new, Collectors.toList()));

			// (2.
			TreeMap<LocalDate, List<Ssdsh>> mapType2 = ssdshes.stream()
					.collect(Collectors.groupingBy(s -> LocalDate.parse(s.getShdate()),
							() -> new TreeMap<>(Comparator.comparing(LocalDate::toEpochDay)), // Use natural ordering for LocalDate
							Collectors.toList()));
			
			everyDaySsdshs = mapType.entrySet().stream().map(SingleDaySsdsh::new).collect(Collectors.toList());
		}

		public List<SingleDaySsdsh> getEveryDaySsdshs() {
			return everyDaySsdshs;
		}
	}

	// Main class to demonstrate the usage
	public static void main(String[] args) {
		List<Ssdsh> ssdshes = new ArrayList<>();
		ssdshes.add(new Ssdsh("2024-07-01", "AM"));
		ssdshes.add(new Ssdsh("2024-07-02", "AM"));
		ssdshes.add(new Ssdsh("2024-07-01", "PM"));
		ssdshes.add(new Ssdsh("2024-07-01", "PM"));

		ssdshes.add(new Ssdsh("2024-07-03", "AM"));
		ssdshes.add(new Ssdsh("2024-07-02", "PM"));

		DaysSsdsh daysSsdsh = new DaysSsdsh(ssdshes);
		List<SingleDaySsdsh> result = daysSsdsh.getEveryDaySsdshs();

		for (SingleDaySsdsh singleDaySsdsh : result) {
			System.out.println(singleDaySsdsh);
		}

	}

}
